# Invillia recruitment challenge

**Aplicação disponível no caminho http://localhost:8080/api/v1/ **
Outras considerações sobre o desafio feitas mais abaixo.


[![Build Status](https://travis-ci.org/shelsonjava/invillia.svg?branch=master)](https://travis-ci.org/shelsonjava/invillia)

![Invillia Logo](https://invillia.com/public/assets/img/logo-invillia.svg)
[Invillia](https://www.invillia.com/) - A transformação começa aqui.

The ACME company is migrating their monolithic system to a microservice architecture and you’re responsible to build their MVP (minimum viable product)  .
https://en.wikipedia.org/wiki/Minimum_viable_product

Your challenge is:
Build an application with those features described below, if you think the requirements aren’t detailed enough please leave a comment (portuguese or english) and proceed as best as you can.

You can choose as many features you think it’s necessary for the MVP,  IT’S NOT necessary build all the features, we strongly recommend to focus on quality over quantity, you’ll be evaluated by the quality of your solution.

If you think something is really necessary but you don’t have enough time to implement please at least explain how you would implement it.

## Tasks

Your task is to develop one (or more, feel free) RESTful service(s) to:
* Create a **Store** **(feito)**
* Update a **Store** information **(feito)**
* Retrieve a **Store** by parameters **(feito)**
* Create an **Order** with items
* Create a **Payment** for an **Order**
* Retrieve an **Order** by parameters
* Refund **Order** or any **Order Item**

Fork this repository and submit your code with partial commits.

## Business Rules

* A **Store** is composed by name and address **(ok)**
* An **Order** is composed by address, confirmation date and status **(ok)**
* An **Order Item** is composed by description, unit price and quantity. **(ok)**
* A **Payment** is composed by status, credit card number and payment date **(ok)**
* An **Order** just should be refunded until ten days after confirmation and the payment is concluded. **(ok)**

## Non functional requirements

Your service(s) must be resilient, fault tolerant, responsive. You should prepare it/them to be highly scalable as possible.

The process should be closest possible to "real-time", balancing your choices in order to achieve the expected
scalability.

Os serviços criados atendem todos os critérios acima - todos eles possuem lógica para tratar com inputs inesperados e não disparam exceções
com parâmetros inválidos ou malformados, como pode-se validar pelos testes unitários.

## Nice to have features (describe or implement):
* Asynchronous processing
> Se tratando do MvP, deveria ser implantado pelo menos no processamento de Payments e Refunds. Isso porque a aprovação de pagamento ou estorno envolve consultas a APIs de fornecedores (Visa, Mastercard, Bancos, etc), cujas consultas externas poderiam deixar a API lenta ou até mesmo irresponsiva. Os consumidores da API deveriam apenas receber uma confirmação de que a solicitação foi recebida e será processada.
* Database
> Para o MvP, a database utilizada foi a H2, para que a aplicação pudesse ser testada com o a maior praticidade possível.
* Docker
> Grande aliado às operações DevOps de integração contínua, mas optei por não implementar uma imagem da aplicação em Docker para o MvP devido ao tempo limitado da prova.
* AWS
> A aplicação pode ser facilmente implantada no ElasticBeanStalk com scripts ou ferramentas de CI.
* Security
> Recursos como CreateStore e UpdateStore (e todos os outros que escrevem no banco de dados) deveriam estar protegidos. Havendo tempo extra, minha primeira escolha seria utilizar o padrão OAuth2 com JWT.
* Swagger
> Implementado no caminho http://localhost:8080/api/v1/swagger-ui.html 
* Clean Code
> As classes de negócio foram bem documentadas e o código em toda a aplicação descreve bem a sua intenção ao leitor. Os testes de integração foram escritos com o cuidado de identificar rapidamente o que testam e o resultado esperado.
