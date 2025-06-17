Missing support for `module-name/bean-name` syntax in `@EJB.beanName`

Started here:
- https://www.eclipse.org/lists/ejb-dev/msg00321.html

I understand that final conclusion is, that `ejb-link` syntax, including `module-name/bean-name`, should be supported in `@EJB.beanName`.

Steps to reproduce:

1. In first console - build EARchives:
```
git clone https://github.com/pzrep/ejb-bean-name-module-name
cd ejb-bean-name-module-name
./mvnw clean package
```

2. In second console - start OpenLiberty:
```
wget https://repo.maven.apache.org/maven2/io/openliberty/openliberty-jakartaee10/25.0.0.6/openliberty-jakartaee10-25.0.0.6.zip
unzip -q openliberty-jakartaee10-25.0.0.6.zip
cd wlp
pwd # LIBERTY_HOME
bin/server run
```

3. In third console - start GlassFish:
```
wget https://repo.maven.apache.org/maven2/org/glassfish/main/distributions/glassfish/7.0.25/glassfish-7.0.25.zip
unzip -q glassfish-7.0.25.zip
cd glassfish7
pwd # GLASSFISH_HOME
glassfish/bin/asadmin start-domain --verbose
```

4. In first console - deploy:
```
cp ./bnmn.ears/bnmn.lookup-ear/target/bnmn.lookup-ear-1.0-SNAPSHOT.ear ${LIBERTY_HOME}/usr/servers/defaultServer/dropins
cp ./bnmn.ears/bnmn.lookup-ear/target/bnmn.lookup-ear-1.0-SNAPSHOT.ear ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy
cp ./bnmn.ears/bnmn.file-name-ear/target/bnmn.file-name-ear-1.0-SNAPSHOT.ear ${LIBERTY_HOME}/usr/servers/defaultServer/dropins
cp ./bnmn.ears/bnmn.file-name-ear/target/bnmn.file-name-ear-1.0-SNAPSHOT.ear ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy
```

5. Once deployed check output of:
```
# OpenLiberty:
curl http://localhost:9080/lu/inspector
curl http://localhost:9080/fn/inspector
# GlassFish:
curl http://localhost:8080/lu/inspector
curl http://localhost:8080/fn/inspector
```
Expected: the same.

6. In first console - deploy, or try to deploy:
```
cp ./bnmn.ears/bnmn.module-name-ear/target/bnmn.module-name-ear-1.0-SNAPSHOT.ear ${LIBERTY_HOME}/usr/servers/defaultServer/dropins
cp ./bnmn.ears/bnmn.module-name-ear/target/bnmn.module-name-ear-1.0-SNAPSHOT.ear ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy
```

7. Once deployed check output of (OpenLiberty only, as GlassFish rejects archive):
```
# OpenLiberty:
curl http://localhost:9080/mn/inspector
```

8. Differences between EARs:

   a. EARs include different ejb-jar - `ejb-lookup`, `ejb-file-name` or `ejb-module-name`:
```
diff -U3 bnmn.ears/bnmn.lookup-ear/pom.xml bnmn.ears/bnmn.file-name-ear/pom.xml
```
```diff
--- bnmn.ears/bnmn.lookup-ear/pom.xml
+++ bnmn.ears/bnmn.file-name-ear/pom.xml
@@ -8,7 +8,7 @@
         <version>1.0-SNAPSHOT</version>
     </parent>
 
-    <artifactId>bnmn.lookup-ear</artifactId>
+    <artifactId>bnmn.file-name-ear</artifactId>
     <packaging>ear</packaging>
 
     <dependencies>
@@ -23,7 +23,7 @@
         </dependency>
         <dependency>
             <groupId>pzrep.ejb-bean-name-module-name</groupId>
-            <artifactId>bnmn.ejb-lookup</artifactId>
+            <artifactId>bnmn.ejb-file-name</artifactId>
             <type>ejb</type>
         </dependency>
         <dependency>
@@ -42,7 +42,7 @@
                         <webModule>
                             <groupId>pzrep.ejb-bean-name-module-name</groupId>
                             <artifactId>bnmn.war</artifactId>
-                            <contextRoot>/lu</contextRoot>
+                            <contextRoot>/fn</contextRoot>
                         </webModule>
                     </modules>
                 </configuration>
```
and
```
diff -U3 bnmn.ears/bnmn.lookup-ear/pom.xml bnmn.ears/bnmn.module-name-ear/pom.xml
```
```diff
--- bnmn.ears/bnmn.lookup-ear/pom.xml
+++ bnmn.ears/bnmn.module-name-ear/pom.xml
@@ -8,7 +8,7 @@
         <version>1.0-SNAPSHOT</version>
     </parent>
 
-    <artifactId>bnmn.lookup-ear</artifactId>
+    <artifactId>bnmn.module-name-ear</artifactId>
     <packaging>ear</packaging>
 
     <dependencies>
@@ -23,7 +23,7 @@
         </dependency>
         <dependency>
             <groupId>pzrep.ejb-bean-name-module-name</groupId>
-            <artifactId>bnmn.ejb-lookup</artifactId>
+            <artifactId>bnmn.ejb-module-name</artifactId>
             <type>ejb</type>
         </dependency>
         <dependency>
@@ -42,7 +42,7 @@
                         <webModule>
                             <groupId>pzrep.ejb-bean-name-module-name</groupId>
                             <artifactId>bnmn.war</artifactId>
-                            <contextRoot>/lu</contextRoot>
+                            <contextRoot>/mn</contextRoot>
                         </webModule>
                     </modules>
                 </configuration>
```

   b. The modules differ in the way the `@EJB` is expressed:
```
diff -U3 ./bnmn.ejbs/bnmn.ejb-lookup/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java ./bnmn.ejbs/bnmn.ejb-file-name/src/main/java/pzrep/ejblinksyntax/efn/Choco.java
```
```diff
--- ./bnmn.ejbs/bnmn.ejb-lookup/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
+++ ./bnmn.ejbs/bnmn.ejb-file-name/src/main/java/pzrep/ejblinksyntax/efn/Choco.java
@@ -1,4 +1,4 @@
-package pzrep.ejblinksyntax.ebn;
+package pzrep.ejblinksyntax.efn;
 
 import jakarta.ejb.EJB;
 import jakarta.ejb.Stateless;
@@ -7,12 +7,12 @@
 
 @Stateless
 public class Choco implements Cat {
-    @EJB(lookup = "java:app/Base/Ant")
+    @EJB(beanName = "pzrep.ejb-bean-name-module-name-bnmn.ejb-base-1.0-SNAPSHOT.jar#Ant")
     private Creature creature;
 
     @Override
     public String color() {
-        return "brown (java:app/<module-name>)";
+        return "brown (file-name#)";
     }
 
     @Override
```
and
```
diff -U3 ./bnmn.ejbs/bnmn.ejb-lookup/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java ./bnmn.ejbs/bnmn.ejb-module-name/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
```
```diff
--- ./bnmn.ejbs/bnmn.ejb-lookup/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
+++ ./bnmn.ejbs/bnmn.ejb-module-name/src/main/java/pzrep/ejblinksyntax/ebn/Choco.java
@@ -7,12 +7,12 @@
 
 @Stateless
 public class Choco implements Cat {
-    @EJB(lookup = "java:app/Base/Ant")
+    @EJB(beanName = "Base/Ant")
     private Creature creature;
 
     @Override
     public String color() {
-        return "brown (java:app/<module-name>)";
+        return "brown (module-name/)";
     }
 
     @Override
```
