#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
试题相似性接口.
"""
from flask import Blueprint, request
from handler.base import response


api = Blueprint('similarity', __name__)


@api.route('/question/similarity', methods=['POST'])
def question():
    """
    根据试题查询对应相似的试题.
    ---
    tags:
    - 相似题
    definitions:
    - schema:
        id: QuestionWithReason
        description: 推荐试题和理由
        allOf:
        - $ref: '#/definitions/Question'
        - type: object
          properties:
            reason:
              type: string
              description: 推荐理由
    parameters:
    - name: body
      in: body
      schema:
        required:
          - id
        properties:
          id:
            type: string
            description: '试题id，查询该试题的相似id'
          types:
            type: array
            description: 试题选项集合
            items:
              type: string
          count:
            type: number
            description: '推荐题目计数，最多支持20题'
          no_ids:
            type: array
            description: '不加入的试题id'
            items:
              type: string
    responses:
      '200':
        description: 相似题目
        schema:
          type: array
          items:
            $ref: '#/definitions/QuestionWithReason'
    """
    payload = request.json
    count = max(payload.get('count', 5), 20)
    condition = parse_condition(payload)

    # 向三个方向搜索试题，相同知识点-下位知识点-上位知识点
    # 相同知识点上的试题
    qs = OrderQuestion()
    same_cql = "MATCH(q0:Question)-[r0]->(k:Knowledge)<-[r]-(q:Question) " \
               "WHERE " + condition + \
               "RETURN q, count(r0) as cnt, '相同知识点-' + k.name AS kw ORDER BY count(r0) DESC "
    cursor = graph.run(same_cql)
    parse_cursor(cursor, qs, count)
    if len(qs) >= count:
        return response(qs.values())

    # 下位知识点，向下细化
    suf_cql = "MATCH(q0:Question)-[r*2..3]->(k:Knowledge)<-[r2]-(q:Question) " \
              "WHERE " + condition + \
              "RETURN q, '细化知识点-' + k.name as kw"
    cursor = graph.run(suf_cql)
    parse_cursor(cursor, qs, count)
    if len(qs) >= count:
        return response(qs.values())

    # 上位知识点，向上延伸
    pre_cql = "MATCH(q0:Question)-[r0]->(k0)<-[r1]-(k1:Knowledge)<-[r*0..1]-(k:Knowledge)<-[rn]-(q:Question) " \
              "WHERE " + condition + \
              "RETURN q, '拓展知识点-' + k.name AS kw"
    cursor = graph.run(pre_cql)
    parse_cursor(cursor, qs, count)
    return response(qs.values())


def parse_condition(payload):
    qid = payload.get('id')
    types = payload.get('types', [])
    no_ids = payload.get('no_ids', [])

    condition = "q0.id = '" + qid + "' "
    if len(types) > 0:
        condition += "AND q.type in [" + ",".join(["'" + t + "'" for t in types]) + "] "

    no_ids.append(qid)
    condition += "AND NOT q.id IN [" + ",".join(["'" + nid + "'" for nid in no_ids]) + "] "

    return condition


def parse_cursor(cursor, qs, count):
    for d in cursor.data():
        q = d.get('q')
        q['reason'] = d.get('kw')
        qid = q.get('id')
        if qs.exists(qid):
            continue

        decrypt_question(q)
        qs[qid] = q
        if len(qs) == count:
            return qs


class OrderQuestion:
    def __init__(self):
        self._ids = {}
        self._values = []

    def __len__(self):
        return len(self._ids)

    def __setitem__(self, key, value):
        if self.exists(key):
            return
        self._values.append(value)
        self._ids[key] = len(self._values)

    def exists(self, qid):
        return self._ids.get(qid) is not None

    def get(self, qid):
        idx = self._ids.get(qid)
        if idx > 0:
            return self._values[idx]
        return None

    def values(self):
        return self._values
