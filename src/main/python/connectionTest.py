#pip install py4j
from py4j.java_gateway import JavaGateway
from py4j.java_collections import SetConverter, MapConverter, ListConverter
gateway = JavaGateway()
listConverter = ListConverter()
nicknames = ["Pippo", "Topolino"]
server=gateway.entry_point
random=gateway.jvm.java.util.Random()
currentPlayer=0
while not server.isGameEnded():
    if server.getCurrentPlayer() == nicknames[currentPlayer]:
        server.makeMove(
            listConverter.convert([random.nextInt(9), random.nextInt(9)], gateway._gateway_client),
            1,
            random.nextInt(5),
            nicknames[currentPlayer])
    currentPlayer=(currentPlayer+1)%2
print("Game done")