# Software Distribuit (2015) - Grup F11

---

## Set i Mig

Quan es generen els artefactes del projecte, aquests es guardan a la carpeta `out\artifacts` de l'arrel
del repositari. 

### Servidor Multi-fil

`java -jar ./out/artifacts/sevenhalf_bank_jar/sevenhalf-bank.jar -p 1212 -b 100 -f ./out/artifacts/sevenhalf_bank_jar/deck.txt`

### Servidor Selector

`java -jar ./out/artifacts/sevenhalf_bank_selector_jar/sevenhalf-bank.jar -p 1212 -b 100 -f ./out/artifacts/sevenhalf_bank_selector_jar/deck.txt`

### Client

`java -jar ./out/artifacts/sevenhalf_player_jar/sevenhalf-bank.jar -s 127.0.0.1 -p 1212`

---

## Exercicis amb Tomcat

S'ha de tenir, a nivell de Intellij, un servidor d'aplicacions configurat cap a l'arrel del servidor Tomcat cap el
que es desplegarà el WAR:

![Configuració](https://dl.dropboxusercontent.com/u/9123154/cdn/uni/sd/intellij-tomcat-server-conf.PNG)

Hi han tres servlets mapejats:

- Counter
- GuessNumber
- PostRedirectGet
