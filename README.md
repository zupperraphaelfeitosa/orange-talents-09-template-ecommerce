## DESAFIO: Implementar o desafio de ecommerce do Mercado Livre

- ### Rescurso [/usuarios], Methods: POST
  ```
  POST: /usuarios: para cadastrar uma novo usuário
  ```

- ### Responses
  | Código | Descrição |
    |---|---|
  | `200` | Requisição executada com sucesso (Success).|
  | `400` | Erros de validação ou cadastro existente (Bad Request).|

### Criar usuário

**POST:** `http://localhost:8080/api/v1/usuarios` com *body*:

- Request (application/json)
    ```json
    {
      "email":"johndoe@gmail.com",
      "senha":"123456"
    }
    ```

- Response 200 (Success)

- Response 400 (Bad Request) - Erros na validação
    ```json
    {
        "campo": "email",
        "error": "deve ser um endereço de e-mail bem formado"
    },
    {
        "campo": "senha",
        "error": "A senha deve ter no minimo 6 caracteres"
    }  
    ```