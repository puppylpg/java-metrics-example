/**
 * 大前提：默认情况下，子logger将继承父logger的所有appenders。
 *
 * 1. 所有的xxxDemo都没有自己的appender，因此会继承root的appender：stdout & fileout.
 * 因此在运行xxxDemo的时候，这两个appender都会输出东西，具体表现为：命令行在输出日志，fileout.log也在打日志。
 *
 * 2. 所有的xxxRollingDemo都有自己的appender：RoolingAppender，并且，他们也都会继承root的appender：stdout & fileout.
 * 因此在运行xxxRollingDemo的时候，命令行在输出日志，fileout.log在打日志，各个rolling日志文件也在打日志。
 *
 * 还可以分析另一种情况，会更加清晰：
 * private static final Logger learnFromGorgon = Logger.getLogger("invokeByName");
 * 在Log4jDemo.java中搞了一个按照名字获取的appender。
 * 由于learnFromGorgon会继承父类root的两个appender，
 * 因此learnFromGorgon打的日志会在三个地方输出：console & fileout.log & invokeByName.log。
 *
 * 但是invokeByName不是任何其他logger的父类，所以别的日志不会输出到invokeByName.log中。
 * （除非给它定义一个子logger，如：Logger son = Logger.getLogger("invokeByName.xxx")，
 * 那么son在往自己的日志里输出的时候，也会往invokeByName.log中输出日志。）
 *
 * 故而：使用名字去获取logger好像比较简单也比较清晰，大家都不具备继承关系，都是兄弟节点，不会出现日志混输的情况。（目前个人是这么理解的）
 * 而且，大家都是第一代孩子，那些没定义appender的logger不会是这些自定义名字的子类，
 * 所以那些logger只会继承root的appender，不会将日志输入到这些log中。
 *
 * 比如，Gorgon里的各个日志（使用log4j），基本都是用名字去获取，就都是兄弟，互不掺和：
 * private static final Logger performanceLog = Logger.getLogger("performanceLog"); 打在performance.log
 * private final static Logger accessLog = Logger.getLogger("accessLog");           打在accesslog
 * 而且，对于其他的logger: LogFactory.getLog(class)，默认只有root是父类，因此只会将日志打到logs/log中。
 * <root>
 *     <priority value="INFO" />
 *     <appender-ref ref="NORMAL" />
 * </root>
 *
 *
 *
 * 最后提一点：slf4j由于目前只配了`logback-classic`，所以slf4j只有logback一个实现类，默认logback.xml就是它的配置文件。
 * 想更改实现类的话，直接删掉`logback-classic`，换成其他的，并相应添加个配置文件就行了，代码完全不用动！
 * 的确挺方便的。（可以换成slf4j-simple、jul-to-slf4j、slf4j-jdk14、slf4j-log4j12等）
 */
package example.logs;