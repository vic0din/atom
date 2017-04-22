#HSLIDE
# Java
lecture 8
## IO, Serialization

#HSLIDE
## Отметьтесь на портале
https://atom.mail.ru/

#HSLIDE
### get ready
```bash
> git fetch upstream
> git checkout -b lecture09 upstream/lecture09
```
Refresh gradle project


#HSLIDE
## Agenda
1. IO/NIO
1. Serialization
1. Collections revisited
1. Exceptions revisited
1. Docker


#HSLIDE
## Agenda
1. **[IO/NIO]**
1. Serialization
1. Collections revisited
1. Exceptions revisited
1. Docker


#HSLIDE
## IO
(http://docs.oracle.com/javase/tutorial/essential/io/)[http://docs.oracle.com/javase/tutorial/essential/io/]
API for input and output to
- files
- network streams
- internal memory buffers
- ...
IO API is **blocking**


#HSLIDE
## Agenda
1. IO/NIO
1. **[Serialization]**
1. Collections revisited
1. Exceptions revisited
1. Docker

#HSLIDE
##Byte Streams
### InputStream
source -> InputStream  
AudioInputStream, ByteArrayInputStream, FileInputStream, FilterInputStream, ObjectInputStream, PipedInputStream, SequenceInputStream, StringBufferInputStream
### OutputStream
program -> OutputStream  
ByteArrayOutputStream, FileOutputStream, FilterOutputStream, ObjectOutputStream, PipedOutputStream
  
IO API is **blocking**
> @see io.ByteStreams.java
  
> @see System.out / System.err (PrintStream)

#HSLIDE
##Character streams
### Reader
source --> Reader  
BufferedReader, CharArrayReader, FilterReader, InputStreamReader, PipedReader, StringReader
### Writer
Writer --> target  
BufferedWriter, CharArrayWriter, FilterWriter, OutputStreamWriter, PipedWriter, PrintWriter, StringWriter

IO API is **blocking**
> @see io.CharacterStreams.java

#HSLIDE
## NIO
Source -async-> Channel --> Buffer  
Buffer --> Channel -async-> Target  
  
NIO API is **non-blocking**  
**details:** (http://tutorials.jenkov.com/java-nio/index.html)[http://tutorials.jenkov.com/java-nio/index.html]

#HSLIDE
## sniff tcp traffic with tcpdump
[http://www.tcpdump.org/](http://www.tcpdump.org/)  
tcpdump - standard unix tool to for traffic analysis
```bash
> tcpdump -Aq -s0 -i lo0 'tcp port 8090'
```

#HSLIDE
## Another nice tools
**tcpflow**  
[tcpflow on github](https://github.com/simsong/tcpflow)  
**wireshark**  
[home page](https://www.wireshark.org/)


#HSLIDE
## Agenda
1. IO/NIO
1. Serialization
1. **[Collections revisited]**
1. Exceptions revisited
1. Docker

#HSLIDE
1. Нарисуйте иерархию классов коллекций
2. ArrayList - устройство и асимптотика
3. LinkedList - устройство и асимптотика
4. HashMap - устройство и асимптотика
5. какие требования предъявляются к объектам, помещаемым в hashmap
6. какие требования предъявляются к объектам, помещаемым в treemap

#HSLIDE
## Agenda
1. IO/NIO
1. Serialization
1. Collections revisited
1. **[Exceptions revisited]**
1. Docker

#HSLIDE
1. Нарисуйте иерархию исключений
2. checked и unchecked exceptions
3. что будет с исключением, выкинутым из блока finally?
4. что такое try-with-resources?
5. что будет с исключением, выкинутым при закрытии ресурса?

#HSLIDE
## Agenda
1. IO/NIO
1. Serialization
1. Collections revisited
1. Exceptions revisited
1. **[Docker]**

#HSLIDE
**Оставьте обратную связь**
(вам на почту придет анкета)  

**Это важно!**
