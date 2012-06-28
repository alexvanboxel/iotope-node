IOTOPE_NODE_HOME=$(cd $(dirname "$0")/..; pwd)
export  IOTOPE_NODE_HOME
java -jar $IOTOPE_NODE_HOME/lib/iotope-node-bootstrap-${project.version}.jar
