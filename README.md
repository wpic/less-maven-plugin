Fast maven plugin for less compiler (use jless). It can also convert multiple files and merge them to one.

# Sample usage

```xml
<plugin>
    <groupId>com.github.wpic</groupId>
    <artifactId>less-maven-plugin</artifactId>
    <version>1.5</version>
    <configuration>
        <compiles>
            <!-- First compile -->
            <compile>
                <from>bower_components/bootstrap-less/less/bootstrap.less</from>
                <to>${project.build.directory}/${project.build.finalName}/bower_components/bootstrap-less/less/bootstrap.css</to>
                <compress>true</compress>
            </compile>
            <!-- Append another one to it -->
            <compile>
                <from>bower_components/bootstrap-less/less/theme.less</from>
                <append>${project.build.directory}/${project.build.finalName}/bower_components/bootstrap-less/less/bootstrap.css</append>
                <compress>true</compress>
            </compile>
            <!-- Add all the less files using wildcard -->
            <compile>
                <from>mithril_components/**/*.less</from>
                <append>${project.build.directory}/${project.build.finalName}/bower_components/bootstrap-less/less/bootstrap.css</append>
                <compress>true</compress>
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
```

# Parameters

* from: Source less file.
* to: Destination CSS file.
* append: Append the CSS to the other file (if 'to' does not set).
* compress: Compress the output or not.

# History

**v1.5**
* Yodate to jlessc version 1.5

**v1.2**
* Update to jlessc version 1.2
