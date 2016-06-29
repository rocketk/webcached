cd `dirname $0`
cd ..
echo `pwd`
echo mvn clean install -Dmaven.test.skip=true
mvn clean install -Dmaven.test.skip=true