#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
章节课程，包容如下内容:

- 根据学生查询错题集
- 学生做题情况，更新错题集
- 学生知识点掌握情况
- 学生查漏补缺(推荐的原因)
"""


from flask import Blueprint

from handler.base import response

api = Blueprint('chapter', __name__)


@api.route('/textbook/list', methods=['GET'])
def textbooks():
    """
    查询教材列表.
    ---
    tags:
    - 章节
    definitions:
    - schema:
        id: Textbook
        properties:
          id:
            type: string
            description: 教材id
          name:
            type: string
            description: 教材名称
    responses:
      '200':
        description: 查询到的试题
        schema:
          type: array
          items:
            $ref: '#/definitions/Textbook'
    """
    return response({})


@api.route('/textbook/<tid>/chapter', methods=['GET'])
def chapter(tid):
    """
    指定教材的章节.
    ---
    tags:
    - 章节
    definitions:
    - schema:
        id: Section
        properties:
          id:
            type: string
            description: 节id
          name:
            type: string
            description: 节名称
    - schema:
        id: Chapter
        properties:
          id:
            type: string
            description: 章id
          name:
            type: string
            description: 章名称
          sections:
            type: array
            items:
              $ref: '#/definitions/Section'
            description: 节列表
    parameters:
    - name: tid
      in: path
      description: 教材id
      required: true
      type: string
    responses:
      '200':
        description: 章节列表
        schema:
          type: array
          items:
            $ref: '#/definitions/Chapter'
    """
    return response({})
