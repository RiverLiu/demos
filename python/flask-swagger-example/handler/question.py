#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 历史课程

from flask import Blueprint

from handler.base import response

api = Blueprint('history', __name__)


@api.route('/question/<qid>', methods=['GET'])
def get_question(qid):
    """
    根据试题id查询试题.
    ---
    tags:
    - 试题
    definitions:
    - schema:
        id: Option
        properties:
          key:
            type: string
            description: 选项
          value:
            type: string
            description: 选项取值
    - schema:
        id: Question
        properties:
          id:
            type: string
            description: 试题id
          question:
            type: string
            description: 试题题干
          type:
            type: string
            description: 试题类型
          complexity:
            type: number
            description: '试题复杂度，浮点型'
          option:
            type: array
            items:
              $ref: '#/definitions/Option'
            description: 试题选项
          knowledge:
            type: string
            description: 试题所属知识点
          answer:
            type: string
            description: 答案
          analysis:
            type: string
            description: 试题分析
          created_at:
            type: string
            description: '试题创建时间,字符串格式'
          updated_at:
            type: string
            description: '试题更新时间,字符串格式'
    parameters:
    - name: qid
      in: path
      description: 试题id
      required: true
      type: string
    responses:
      '200':
        description: 查询到的试题
        schema:
          type: Question
          $ref: '#/definitions/Question'
    """
    return response({})


@api.route('/question/<qid>/knowledge', methods=['GET'])
def get_question_knowledge(qid):
    """
    根据试题id查询试题的知识点.
    ---
    tags:
      - 试题
    parameters:
    - name: qid
      in: path
      description: 试题id
      required: true
      type: string
    responses:
      '200':
        description: 查询到的知识点列表
        schema:
          type: array
          items:
            type: string
   """
    return response({})


@api.route('/question/list', methods=['GET'])
def list_questions():
    """
    查询试题列表.
    ---
    tags:
      - 试题
    parameters:
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
        description: 试题列表
        schema:
          type: array
          items:
            $ref: '#/definitions/Question'
    """
    return response({})


@api.route('/knowledge/questions', methods=['GET'])
def get_question_by_knowledge():
    """
    根据知识点查询试题
    ---
    tags:
    - 知识点
    definitions:
    - schema:
        id: Knowledge
        properties:
          name:
            type: string
            description: 知识点名称
          created_at:
            type: string
            description: '试题创建时间,字符串格式'
          updated_at:
            type: string
            description: '试题更新时间,字符串格式'
    parameters:
    - name: k
      in: query
      description: k 知识点名称
    responses:
      '200':
        description: 试题列表
        schema:
          type: array
          items:
            $ref: '#/definitions/Question'
    """
    return response({})


@api.route('/knowledge/list', methods=['GET'])
def list_knowledge():
    """
    查询知识点列表.
    ---
    tags:
    - 知识点
    parameters:
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
         description: 知识点列表
         schema:
           type: array
           items:
             $ref: '#/definitions/Knowledge'
    """
    return response({})
