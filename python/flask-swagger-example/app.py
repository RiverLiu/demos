# -*- coding: utf-8 -*-

import os
import sys
from pathlib import Path
from flask import Flask, jsonify, render_template
from flask_swagger import swagger

import config

# set sys path
root = Path(__file__).absolute().parent.parent
sys.path.extend([os.path.join(root, '.'), os.path.join(root, 'recommend')])


# start flask app
app = Flask('recommend')


def register_api():
    # set up rest api
    from handler import apis
    for api in apis:
        app.register_blueprint(api)


@app.route('/')
def index():
    return render_template("index.html")


@app.route('/swagger')
def swagger_index():
    return render_template("swagger/index.html", **{
        "url": "http://" + config.IP_ADDRESS + ":" + str(config.PORT) + "/spec"
    })


@app.route('/spec')
def spec():
    swag = swagger(app)
    swag['info']['title'] = 'Recommend System API'
    swag['info']['version'] = '1.0'
    return jsonify(swag)


if __name__ == "__main__":
    """
    Flask-Swagger 示例.
    """
    register_api()

    # start application
    app.run(debug=config.DEBUG,
            host=config.IP_ADDRESS,
            port=config.PORT,
            threaded=True)
