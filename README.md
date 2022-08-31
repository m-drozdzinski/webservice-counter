
# Demo web service - counter

## Starting instructions

Service can be started with following commands. Idea is to run scripts from project's `<root_directory>`

```
git clone https://github.com/m-drozdzinski/webservice-counter.git
cd webservice-counter
./scripts/run_service.sh
```

## Manual testing

There are some examples how to interact with service in directory:  `/manual_tests`

## Api documentation

Api can be found in `documentation` directory. If you followed [starting instruction](#starting-instruction) you should be able to open swagger ui with `http://localhost:8080/swagger-ui.html`

## Troubleshooting
You may need to install java 17 for this project. Please follow instruction from [sdkman.io](https://sdkman.io/)
```
curl -s "https://get.sdkman.io" | bash
source "/home/<username>/.sdkman/bin/sdkman-init.sh"
sdk install java
```