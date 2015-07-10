Fast maven plugin for less compiler (use jless). It can also convert multiple files and append them to the other files.

# Sample usage

```xml
<build>
  ...
  <plugins>
     ...
     <plugin>
        <groupId>com.github.wpic</groupId>
        <artifactId>less-maven-plugin</artifactId>
        <version>1.0</version>
        <configuration>
           <compiles>
              <compile>
                 <from>${basedir}/less/bootstrap-all.less</from>
                 <to>${project.build.directory}/${project.build.finalName}/css/all.css</to>
                 <compress>true</compress>
              </compile>
              <compile>
                 <from>${basedir}/bower_components/bootstrap-less/less/theme.less</from>
                 <to>${project.build.directory}/${project.build.finalName}/css/all.css</to>
                 <compress>false</compress>
              </compile>
           </compiles>
        </configuration>
        <executions>
           <execution>
              <phase>compile</phase>
              <goals>
                 <goal>less</goal>
              </goals>
           </execution>
        </executions>
     </plugin>
     ...
  </plugins>
</build>
```

# Parameters

* from: Source less file.
* to: Destination CSS file.
* append: Append the CSS to the other file (if 'to' does not set).
* compress: Compress the output or not.