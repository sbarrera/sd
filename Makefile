
CLASSPATH_BANKSYSTEM = ./out/artifacts/rmi_banksystem_server_jar/rmi-banksystem-server.jar

banksystem:
	@echo "Running 'RMI - Bank System' example RMI registry service..."
	export CLASSPATH=$(CLASSPATH_BANKSYSTEM)
	rmiregistry
