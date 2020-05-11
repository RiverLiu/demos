#!/usr/bin/env python3
# -*- coding: utf-8 -*-


from flask import request
from flask import Blueprint

from handler.base import response

api = Blueprint('student', __name__)


@api.route('/student/<sid>', methods=['GET'])
def get_student(sid):
    """
    获取学生信息.
    ---
    tags:
    - 学生
    definitions:
    - schema:
        id: Student
        properties:
          id:
            type: string
            description: 学生id
          question:
            type: name
            description: 学生名称
    parameters:
    - name: sid
      in: path
      description: 学生id
      required: true
      type: string
    responses:
      '200':
        description: 查询到的学生
        schema:
          type: Student
          $ref: '#/definitions/Student'
    """
    return response({})


@api.route('/student/list', methods=['GET'])
def list_student():
    """
    获取学生列表
    ---
    tags:
      - 学生
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
      default: 50
    responses:
      '200':
        description: 学生列表
        schema:
          type: array
          items:
            $ref: '#/definitions/Student'
    """
    return response({})
