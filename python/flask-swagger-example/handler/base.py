# coding: utf-8
# Restful api response

from flask import jsonify


def response(content=None, error_code='0', message=''):
    """
    return same message with plat backend system
    :param content: message
    :param error_code: error code
    :param message: error message
    """
    if error_code == '0':
        data = {
            'success': True,
            'errorCode': error_code,
            'data': content
        }
    else:
        data = {
            'success': False,
            'errorCode': error_code,
            'errorMsg': message,
        }
    resp = jsonify(data)

    return resp
