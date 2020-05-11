Flask 配置并使用 Swagger 服务.

# flask-swagger 是什么

在使用之前，我们先来了解下 `flask-swagger` 是什么，如下内容，来自官网。

`flask-swagger` 提供 `swagger`方法，可以扫描 flask 中包含 yaml 格式的文档注释，文档注释格式与 [Swagger 2.0 Operation](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#operation-object) 对象一致。

例如:

```yaml
class UserAPI(MethodView):

    def post(self):
        """
        Create a new user
        ---
        tags:
          - users
        definitions:
          - schema:
              id: Group
              properties:
                name:
                 type: string
                 description: the group's name
        parameters:
          - in: body
            name: body
            schema:
              id: User
              required:
                - email
                - name
              properties:
                email:
                  type: string
                  description: email for user
                name:
                  type: string
                  description: name for user
                address:
                  description: address for user
                  schema:
                    id: Address
                    properties:
                      street:
                        type: string
                      state:
                        type: string
                      country:
                        type: string
                      postalcode:
                        type: string
                groups:
                  type: array
                  description: list of groups
                  items:
                    $ref: "#/definitions/Group"
        responses:
          201:
            description: User created
        """
        return {}
```

`flask-swagger` 支持 `MethodView` 方法级别的文档注释和普通 `flask view` 方法。

底层实现：应用扫描文档，把文档注释中`---`前，第一张内容作为 `summary` 字段，其余行作为`description`字段，`---`之后的文档注释，作为 `Swagger Operation` 对象处理。

支持在注释中添加 [Parameter](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#parameterObject) 和 [Response](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#responsesObject) 支持内联的 [Schema](https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#schemaObject)对象，

另外，docstring 也可以放到额外的文件中。
eg.

```yaml
Create a new user
---
tags:
  - users
definitions:
  - schema:
      id: Group
      properties:
        name:
         type: string
         description: the group's name
parameters:
  - in: body
    name: body
    schema:
      id: User
      required:
        - email
        - name
      properties:
        email:
          type: string
          description: email for user
        name:
          type: string
          description: name for user
        address:
          description: address for user
          schema:
            id: Address
            properties:
              street:
                type: string
              state:
                type: string
              country:
                type: string
              postalcode:
                type: string
        groups:
          type: array
          description: list of groups
          items:
            $ref: "#/definitions/Group"
responses:
  201:
    description: User created
```

使用时，在文档注释中添加：

```py
class UserAPI(MethodView):

    def post(self):
        """
        Create a new user

        blah blah

        swagger_from_file: path/to/file.yml

        blah blah
        """
        return {}
```

`swagger_from_file` 可以替换.

# 安装使用

了解基础之后，开始安装并配置使用，先安装这个库。

```sh
pip install flask-swagger
```

另外，需要在 flask 中配置 spec

```py
@app.route("/spec")
def spec():
    swag = swagger(app)
    swag['info']['version'] = "1.0"
    swag['info']['title'] = "My API"
    return jsonify(swag)
```

现在配置可以生成 json 格式的 OpenAPI 内容了。只有 json 有什么用，我们需要Web页面的Swagger可以访问。另外还需要将 json 放到 swagger-ui 中. swagger-ui 的地址如下： https://github.com/swagger-api/swagger-ui。

`swagger-ui` 项目包含静态的页面，为了方便，我们将静态页面，放到 flask 项目的静态页面中，如下图所示。

![img](https://riverlcn.oss-cn-hangzhou.aliyuncs.com/py/flask_swagger_loc.png)

修改 `swagger index.html`中默认的URL `https://petstore.swagger.io/v2/swagger.json` 改成通过 flask 传入的地址 `{{ url }}`，这样做的目的是，我们可以在配置或者业务中，动态修改URL, 如下。

```html
    window.onload = function() {
      
      // Begin Swagger UI call region
      const ui = SwaggerUIBundle({
        // url: "https://petstore.swagger.io/v2/swagger.json",
        url: "{{ url }}",
        "dom_id": "#swagger-ui",
        ...
      })
```

URL通常包括请求协议，请求地址和端口，这里，我们将这些参数写到配置文件中，这样传递:

```py
@app.route('/swagger')
def swagger_index():
    return render_template("swagger/index.html", **{
        "url": "http://" + config.IP_ADDRESS + ":" + str(config.PORT) + "/spec"
    })
```

最终的效果如下所示

![img](https://riverlcn.oss-cn-hangzhou.aliyuncs.com/py/flask_swagger_web.png))

完整的代码示例，可通过 https://github.com/RiverLiu/demos/tree/master/python/flask-swagger-example 下载.


# 附录

**OpenAPI Specification 2.0**
 
- OpenAPI 说明 2.0 中文版: https://blog.csdn.net/wjc133/article/details/65436778
- 英文原版：https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md

**OpenApi 3.0.3**

- https://swagger.io/specification/

**Notes**

swagger-ui 存在版本兼容的问题，swagger 有 2.0 和 3.0 版本，`flask-swagger 3.18.3` 版支持 swagger 2.0 和 3.0
