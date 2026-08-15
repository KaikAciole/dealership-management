## 2. Endpoints: vehicle-controller

Responsável pela gestão, consulta e manutenção dos dados de veículos.

* **`GET /api/v1/vehicles`**
* **Descrição:** Retorna uma lista paginada de todos os veículos.
* **Query Params:** `page`, `size`, `sort`.
* **Response (200):** `PageVehicleResponse`


* **`GET /api/v1/vehicles/{id}`**
* **Descrição:** Retorna os detalhes de um veículo específico.
* **Path Params:** `id` (UUID).
* **Response (200):** `VehicleResponse`


* **`POST /api/v1/vehicles`**
* **Descrição:** Cria um novo veículo.
* **Request Body:** `VehicleRequest` (application/json)
* **Response (200):** `VehicleResponse`


* **`PUT /api/v1/vehicles/{id}`**
* **Descrição:** Atualiza os dados de um veículo existente.
* **Path Params:** `id` (UUID).
* **Request Body:** `VehicleRequest` (application/json)
* **Response (200):** `VehicleResponse`


* **`DELETE /api/v1/vehicles/{id}`**
* **Descrição:** Exclui um registro de veículo.
* **Path Params:** `id` (UUID).
* **Response (200):** `OK`


* **`PATCH /api/v1/vehicles/{id}/image`**
* **Descrição:** Faz o upload de uma imagem para o veículo.
* **Path Params:** `id` (UUID).
* **Request Body:** `multipart/form-data` contendo o campo `file` (binary).
* **Response (200):** `VehicleResponse`


* **`GET /api/v1/vehicles/search`**
* **Descrição:** Realiza busca filtrada e paginada de veículos.
* **Query Params:** `brand` (string, opcional), `color` (string, opcional), `manufactureYear` (int32, opcional), `pageable` (objeto de paginação).
* **Response (200):** `PageVehicleResponse`



## 3. Endpoints: dealership-controller

Responsável pela gestão das concessionárias parceiras e associação de veículos.

* **`GET /api/v1/dealerships`**
* **Descrição:** Retorna uma lista paginada de concessionárias.
* **Query Params:** Objeto `pageable`.
* **Response (200):** `PageDealershipResponse`


* **`GET /api/v1/dealerships/{id}`**
* **Descrição:** Retorna os detalhes de uma concessionária específica.
* **Path Params:** `id` (UUID).
* **Response (200):** `DealershipResponse`


* **`POST /api/v1/dealerships`**
* **Descrição:** Cria uma nova concessionária.
* **Request Body:** `DealershipRequest` (application/json)
* **Response (200):** `DealershipResponse`


* **`PUT /api/v1/dealerships/{id}`**
* **Descrição:** Atualiza os dados de uma concessionária existente.
* **Path Params:** `id` (UUID).
* **Request Body:** `DealershipRequest` (application/json)
* **Response (200):** `DealershipResponse`


* **`DELETE /api/v1/dealerships/{id}`**
* **Descrição:** Exclui um registro de concessionária.
* **Path Params:** `id` (UUID).
* **Response (200):** `OK`


* **`PATCH /api/v1/dealerships/{id}/status`**
* **Descrição:** Altera o status de atividade (`isActive`) de uma concessionária.
* **Path Params:** `id` (UUID).
* **Response (200):** `DealershipResponse`


* **`GET /api/v1/dealerships/{id}/vehicles`**
* **Descrição:** Lista os veículos associados a uma concessionária específica.
* **Path Params:** `id` (UUID).
* **Response (200):** Objeto de listagem de veículos (`VehicleResponse` ou `PageVehicleResponse`).