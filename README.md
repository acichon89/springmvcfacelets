Spring MVC & Facelets
================
Sample web application with Spring MVC (Controller layer) and Facelets (View layer)

<h2>About</h2>
This simple project shows Spring MVC and Facelets integration. Facelets is the most popular View Framework based on Decorator pattern.
It is a part of JSF component-based specification, but it's hard to mix with other frameworks (like action-based Spring MVC). It comes much easier with
the FaceletViewResolver written by Isaac Silva.
<h2>Page lifecycle</h2>
It goes normally as default Spring MVC process:<br/>
1. Dispatcher Servlet<br/>
2. Handler Mapping<br/>
3. Controller method<br/>
4. ModelAndView <br/>
5. <b>ViewResolver</b> as FaceletViewResolver<br/>
6. Composed View based on Facelet templates.
<h2>How can I run it?</h2>
Just download the project and then run the command as: <br/>
a)<i>mvn tomcat7:run</i> (Tomcat 7 servlet container), or  <br/>
b)<i>mvn jetty:run </i>(Jetty servlet container)
<h2>Credits</h2>
http://www.webdezign.co.uk/ (HTML5 page layout used as example) 
