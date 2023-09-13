# Documentação da API do Aplicativo Mia

Essa documentação serve como um guia para a API do Aplicativo Mia. A API é responsável pelo gerenciamento de Usuários, Tarefas e a interação com APIs externas como OpenAI e Figma para a geração de tarefas personalizadas.

## Funcionamento da API

A API do Aplicativo Mia interage com as APIs da OpenAI e Figma para criar tarefas detalhadas e personalizadas. Inicialmente, a OpenAI produz o contexto e a sequência de passos da tarefa, identificando as interfaces que foram utilizadas. Os nomes dessas interfaces são capturados e enviados à API do Figma. Em seguida, nosso sistema verifica se existe um arquivo Figma relacionado em nosso banco de dados. Se existir, ele percorre todas as interfaces e armazena aquelas usadas na explicação da OpenAI em uma lista. Posteriormente, faz uma nova requisição ao Figma para obter as imagens dessas interfaces. Finalmente, as informações obtidas de ambas as APIs são organizadas pela nossa API, que retorna uma resposta formatada com as imagens das interfaces, seus nomes, o texto explicativo da tarefa e as imagens correspondentes a cada passo. Este processo é iniciado com uma requisição do tipo `POST /ask` à API da Mia.

## Configurações da API

A API tem uma série de configurações que permitem personalizar seu comportamento e integração com o banco de dados, servidor e outras APIs.

### Configurações do Spring JPA

- `spring.jpa.database`: Define o banco de dados usado pelo Spring JPA. Neste caso, é o PostgreSQL.
- `spring.jpa.database-platform`: Define o dialeto do PostgreSQL para o Hibernate.
- `spring.jpa.show-sql`: Habilita a exibição das consultas SQL executadas pelo Spring JPA.
- `spring.jpa.hibernate.ddl-auto`: Configura o comportamento do Hibernate para criação e descarte do esquema do banco de dados.

### Configurações do Banco de Dados

- `spring.database.driverClassName`: Define a classe do driver JDBC para o PostgreSQL.
- `spring.datasource.url`: Define a URL do banco de dados PostgreSQL, com o nome do banco de dados miadb.
- `spring.datasource.username`: Define o nome de usuário para acessar o banco de dados.
- `spring.datasource.password`: Define a senha para acessar o banco de dados.

### Configurações do Servidor

- `server.port`: Define a porta em que o servidor será executado (8080 neste caso).

### Configuração do SQL Init

- `spring.sql.init.mode`: Define o modo de inicialização do SQL, onde 'always' indica que as instruções SQL serão sempre executadas.

### Configurações da API OpenAI

- `openai.api.key`: Insira aqui a chave da API da OpenAI. Certifique-se de substituir "YOUR_OPENAI_KEY_HERE" pela sua chave real.

### Configurações da API Figma

- `figma.api.key`: Define a chave de autenticação para a API do Figma. (OBS: Já está uma KEY colocada no projeto, para caso queira testar, use o exemplo da pergunta "Como trocar meu número do Whatsapp", e passe o nome do aplicativo como "Whatsapp")

## Entidades

### Usuario
- `name` (string): Nome do usuário.
- `email` (string): Endereço de e-mail do usuário.
- `password` (string): Senha do usuário.
- `createdAt` (datetime): Data e hora em que o usuário foi cadastrado.

### Tarefa
- `title` (string): Título da tarefa.
- `steps` (array): Lista de passos detalhados para a execução da tarefa. Cada passo é um objeto que contém um texto gerado pelo GPT-3.
- `applicationId` (integer, obrigatório): ID do Aplicativo a qual a tarefa se refere.
- `createdAt` (datetime): Data e hora em que a tarefa foi criada.

### Aplicação
- `name` (string): Nome da aplicação.
- `description` (string, opcional): Descrição da aplicação.
- `figmaId` (string): Código do projeto no Figma.
- `tasks` (array): Lista de tarefas relacionadas à aplicação. Cada tarefa é um objeto que contém os seguintes campos:
  - `title` (string): Título da tarefa.
  - `steps` (array): Lista de passos detalhados para a execução da tarefa. Cada passo é um objeto que contém um texto gerado pelo GPT-3.
  - `applicationId` (integer, obrigatório): ID da Aplicação à qual a tarefa se refere.
  - `createdAt` (datetime): Data e hora em que a tarefa foi criada.

## Endpoints

### Usuários

#### `POST /users`
Cria um novo usuário.

**Parâmetros do corpo:**
- `name` (string, obrigatório): Nome do usuário.
- `email` (string, obrigatório): Endereço de e-mail do usuário.
- `password` (string, obrigatório): Senha do usuário.

**Respostas:**
- `201 Created`: Usuário criado com sucesso.
- `400 Bad Request`: Parâmetros inválidos.

#### `GET /users`
Lista todos os usuários cadastrados.

**Respostas:**
- `200 OK`: Retorna uma lista de usuários.
- `404 Not Found`: Nenhum usuário encontrado.

#### `GET /users/{id}`
Recupera informações sobre um usuário específico.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID do usuário.

**Respostas:**
- `200 OK`: Retorna informações do usuário.
- `404 Not Found`: Usuário não encontrado.

#### `PUT /users/{id}`
Atualiza informações de um usuário específico.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID do usuário.

**Parâmetros do corpo:**
- `name` (string, opcional): Nome do usuário.
- `email` (string, opcional): Endereço de e-mail do usuário.
- `password` (string, opcional): Senha do usuário.

**Respostas:**
- `200 OK`: Usuário atualizado com sucesso.
- `400 Bad Request`: Parâmetros inválidos.
- `404 Not Found`: Usuário não encontrado.

#### `DELETE /users/{id}`
Exclui um usuário específico.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID do usuário.

**Respostas:**
- `204 No Content`: Usuário excluído com sucesso.
- `404 Not Found`: Usuário não encontrado.

#### `GET /tasks`
Lista todas as tarefas cadastradas.

**Respostas:**
- `200 OK`: Retorna uma lista de tarefas.
- `404 Not Found`: Nenhuma tarefa encontrada.

#### `GET /tasks/{id}`
Recupera informações sobre uma tarefa específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da tarefa.

**Respostas:**
- `200 OK`: Retorna informações da tarefa.
- `404 Not Found`: Tarefa não encontrada.

#### `POST /tasks`
Cria uma nova tarefa.

**Corpo da solicitação:**
- `title` (string, obrigatório): Título da tarefa.
- `createdAt` (string, opcional): Data e hora de criação da tarefa no formato "yyyy-MM-dd'T'HH:mm:ss".
- `applicationId` (integer, opcional): ID da aplicação relacionada à tarefa.
- `steps` (array, opcional): Lista de etapas da tarefa.

**Respostas:**
- `201 Created`: Tarefa criada com sucesso.
- `400 Bad Request`: Parâmetros inválidos.

#### `PUT /tasks/{id}`
Atualiza informações de uma tarefa específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da tarefa.

**Corpo da solicitação:**
- `title` (string, obrigatório): Título da tarefa.
- `createdAt` (string, opcional): Data e hora de criação da tarefa no formato "yyyy-MM-dd'T'HH:mm:ss".
- `applicationId` (integer, opcional): ID da aplicação relacionada à tarefa.
- `steps` (array, opcional): Lista de etapas da tarefa.

**Respostas:**
- `204 No Content`: Tarefa atualizada com sucesso.
- `400 Bad Request`: Parâmetros inválidos.
- `404 Not Found`: Tarefa não encontrada.

#### `DELETE /tasks/{id}`
Exclui uma tarefa específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da tarefa.

**Respostas:**
- `204 No Content`: Tarefa excluída com sucesso.
- `404 Not Found`: Tarefa não encontrada.

### Aplicativos

#### `GET /app`
Lista todas as aplicações cadastradas.

**Respostas:**
- `200 OK`: Retorna uma lista de aplicações.
- `404 Not Found`: Nenhuma aplicação encontrada.

#### `GET /app/{id}`
Recupera informações sobre uma aplicação específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da aplicação.

**Respostas:**
- `200 OK`: Retorna informações da aplicação.
- `404 Not Found`: Aplicação não encontrada.

#### `GET /app/{name}`
Recupera informações sobre uma aplicação específica.

**Parâmetros do caminho:**
- `name` (string, obrigatório): Nome da aplicação.

**Respostas:**
- `200 OK`: Retorna informações da aplicação.
- `404 Not Found`: Aplicação não encontrada.

#### `POST /app`
Cria uma nova aplicação.

**Corpo da solicitação:**
- `name` (string, obrigatório): Nome da aplicação.
- `description` (string, opcional): Descrição da aplicação.
- `figmaId` (string, obrigatório): Código do projeto no Figma.

**Respostas:**
- `201 Created`: Aplicação criada com sucesso.
- `400 Bad Request`: Parâmetros inválidos.

#### `PUT /app/{id}`
Atualiza informações de uma aplicação específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da aplicação.

**Corpo da solicitação:**
- `name` (string, obrigatório): Nome da aplicação.
- `description` (string, opcional): Descrição da aplicação.
- `figmaId` (string, obrigatório): Código do projeto no Figma.

**Respostas:**
- `204 No Content`: Aplicação atualizada com sucesso.
- `400 Bad Request`: Parâmetros inválidos.
- `404 Not Found`: Aplicação não encontrada.

#### `DELETE /app/{id}`
Exclui uma aplicação específica.

**Parâmetros do caminho:**
- `id` (integer, obrigatório): ID da aplicação.

**Respostas:**
- `204 No Content`: Aplicação excluída com sucesso.
- `404 Not Found`: Aplicação não encontrada.

### Perguntas

#### `POST /ask`
Obtém etapas e imagens com base em uma pergunta.

**Corpo da solicitação:**
- `app` (string): Nome do aplicativo.
- `question` (string): Texto da pergunta.

**Respostas:**
- `200 OK`: Retorna uma resposta contendo as etapas e imagens.
- `500 Internal Server Error`: Erro interno do servidor.

**Exemplo de resposta**
```
{
	"steps": {
		"appName": "Whatsapp",
		"question": "Como alterar meu número no Whatsapp?",
		"steps": {
			"Clique no botão de Menu da tela Principal": [
				"Whatsapp-Principal_Menu"
			],
			"Clique na opção Configurações no Menu e depois clique na opção Conta na tela de Configurações": [
				"Whatsapp-Menu_Configuracoes",
				"Whatsapp-Configuracoes_Conta"
			],
			"Na tela de Conta, clique na opção Número": [
				"Whatsapp-Conta_Numero"
			],
			"Clique em \"Alterar número\" na tela de Número e digite o novo número que deseja utilizar": [
				"Whatsapp-Numero_NovoNumero"
			],
			"Clique em \"Próximo\" e confirme o novo número digitado": [
				"Whatsapp-NovoNumero_Confirmar"
			]
		},
		"screens": [
			"Whatsapp-Principal_Menu",
			"Whatsapp-Menu_Configuracoes",
			"Whatsapp-Configuracoes_Conta",
			"Whatsapp-Conta_Numero",
			"Whatsapp-Numero_NovoNumero",
			"Whatsapp-NovoNumero_Confirmar"
		],
		"elements": [
			"Whatsapp-Principal_Menu",
			"Whatsapp-Menu_Configuracoes",
			"Whatsapp-Configuracoes_Conta",
			"Whatsapp-Conta_Numero",
			"Whatsapp-Numero_NovoNumero",
			"Whatsapp-NovoNumero_Confirmar"
		]
	},
	"images": {
		"screens": {
			"err": null,
			"images": {
				"29:33": "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/1fff9806-5615-45aa-af0d-8f26fb60626c",
				"143:2": "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/98daf4cc-7871-42d0-8252-527fd9636a34",
				"145:7": "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/07354575-d2d5-4fbd-9ef0-1fce7989cf53",
				"148:18": "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/14a9999c-f456-4ff3-a1a2-c9f92830c936",
				"150:2": "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/fd96c162-60e7-4d80-b7eb-4b1273f2bd8f"
			}
		}
	}
}
```
