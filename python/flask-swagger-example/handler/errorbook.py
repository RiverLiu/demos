#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
错题集-学生与错题集的关系，包容如下内容:

- 根据学生查询错题集
- 学生做题情况，更新错题集
- 学生知识点掌握情况
- 学生查漏补缺(推荐的原因)
"""

from flask import Blueprint

from handler.base import response

api = Blueprint('errorbook', __name__)


@api.route('/student/<sid>/errors', methods=['GET'])
def get_error_book(sid):
    """
    根据学生查询错题集.
    ---
    tags:
    - 错题集
    parameters:
    - name: sid
      in: path
      description: 学生id
      required: true
      type: string
    - name: pageNum
      in: query
      description: 分页页码, 当前页码
      required: false
      type: int
      default: 1
    - name: pageSize
      in: query
      description: 分页页码, 当前页码
      required: false
      type: int
      default: 10
    responses:
      '200':
        description: 学生的错题集
        schema:
          type: array
          $ref: '#/definitions/Question'
    """
    return response({})


@api.route('/student/<sid>/error/<qid>', methods=['POST'])
def update_error_book(sid, qid):
    """
    更新错题，是否做题正确.
    :return:
    """
    pass


@api.route('/student/<sid>/knowledge', methods=['GET'])
def analysis_all_knowledge(sid):
    """
    学生知识点掌握情况. 在已经做过的试题基础上，统计知识点的掌握情况.
    如果做题数目少于设定值，则不进行计算.
    ---
    tags:
    - 错题集
    parameters:
    - name: sid
      in: path
      description: 学生id
      required: true
      type: string
    responses:
      '200':
        description: 学生的错题集
    """
    return response({})


@api.route('/student/knowledge', methods=['POST'])
def analysis_knowledge():
    """
    学生知识点掌握情况. 在已经做过的试题基础上，统计知识点的掌握情况.
    如果做题数目少于设定值，则不进行计算.
    ---
    tags:
    - 错题集
    parameters:
    - name: body
      in: body
      schema:
        required:
          - sid
        properties:
          sid:
            type: string
            description: 学生id
          cid:
            type: string
            description: 章节id
    responses:
      '200':
        description: 学生的错题集
    """
    return response({})

