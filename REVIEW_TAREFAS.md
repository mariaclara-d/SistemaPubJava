# Revisão rápida da base e tarefas sugeridas

## 1) Tarefa de correção de erro de digitação (typo)
**Problema encontrado:** o wrapper Maven (`mvnw`) estava sem permissão de execução, o que gera falha imediata ao rodar comandos do projeto (`Permission denied`).

**Tarefa sugerida:**
- Ajustar permissões do arquivo `demo/mvnw` para executável no repositório (ex.: `chmod +x demo/mvnw`) e validar no CI.

**Critério de aceite:**
- `./mvnw -v` executa sem erro de permissão.

---

## 2) Tarefa de correção de bug
**Problema encontrado:** os DTOs estão declarados no pacote `dto`, mas os controllers/services importam `com.example.demo.dto.*`. Isso causa erro de compilação por pacote incorreto.

**Tarefa sugerida:**
- Padronizar os pacotes dos DTOs para `com.example.demo.dto` (movendo arquivos e/ou alterando `package`) e corrigir imports.

**Critério de aceite:**
- Projeto compila sem erro de pacote (`package ... does not exist`).
- Endpoints que usam DTOs continuam funcionando.

---

## 3) Tarefa para ajustar comentário/discrepância de documentação
**Problema encontrado:** no `pom.xml`, há comentário `lookup parent from repository`, porém a versão do parent (`4.0.3`) não está resolvendo no ambiente atual (403), gerando inconsistência entre o que o comentário sugere e o que ocorre na prática.

**Tarefa sugerida:**
- Revisar a versão do parent Spring Boot para uma versão disponível/compatível e atualizar comentário/documentação de build para refletir o processo real de resolução de dependências.

**Critério de aceite:**
- `mvn test` consegue resolver o parent POM sem erro de repositório.
- Comentário/documentação de build descreve corretamente a estratégia de resolução.

---

## 4) Tarefa para melhorar teste
**Problema encontrado:** existe apenas um teste de contexto (`contextLoads`) sem validação de regras de negócio.

**Tarefa sugerida:**
- Criar testes unitários de `PubService` cobrindo:
  - adição de item com quantidade inválida (<= 0) não altera a mesa;
  - atualização de produto inexistente retorna `null`;
  - fechamento de conta limpa itens e nickname.

**Critério de aceite:**
- Nova suíte de testes unitários cobrindo os cenários acima com assertions explícitas.
- Cobertura de serviço aumentada em relação ao estado atual.
