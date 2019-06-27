# Jac4e - Jpa Attribute Converter for Enum

[![][maven img]][maven]
[![][javadoc img]][javadoc]
[![][release img]][release]
[![][license img]][license]

[maven]:http://search.maven.org/#search|gav|1|g:"com.github.microtweak"%20AND%20a:"jac4e"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.github.microtweak/jac4e/badge.svg

[javadoc]:https://javadoc.io/doc/com.github.microtweak/jac4e-core
[javadoc img]:https://javadoc.io/badge/com.github.microtweak/jac4e-core.svg

[release]:https://github.com/microtweak/jac4e/releases
[release img]:https://img.shields.io/github/release/microtweak/jac4e.svg

[license]:LICENSE
[license img]:https://img.shields.io/badge/License-MIT-yellow.svg

Annotation processor to improve Enums usage in JPA.

## Problem
JPA 2.1 introduced the [AttributeConverter](https://docs.oracle.com/javaee/7/api/javax/persistence/AttributeConverter.html) interface allowing the use of custom types in JPA entities. This feature brought an alternative to the well-known [@Enumerated](https://docs.oracle.com/javaee/7/api/javax/persistence/Enumerated.html).

However, implementing a new AttributeConverter for each enum is a bit annoying and tiring. Even implementing a generic converter, the [problem](https://stackoverflow.com/questions/23564506) still continues.

## Solution
Introduced an annotation processor that generates the implementation of the AttributeConverter for each enum annotated with @EnumAttributeConverter. This implementation uses a property declared in the enum to do the conversion.

## Usage

1. Setup your pom.xml

```xml
<project>
    <properties>
        <jac4e.version>0.0.1</jac4e.version>
    </properties>

    <dependencies>
        <!-- Other dependencies -->

        <dependency>
            <groupId>com.github.microtweak</groupId>
            <artifactId>jac4e-core</artifactId>
            <version>${jac4e.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Other plugins -->

            <plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
                <version>1.1.3</version>
                <dependencies>
                    <dependency>
                        <groupId>com.github.microtweak</groupId>
                        <artifactId>jac4e-processor</artifactId>
                        <version>${jac4e.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>target/generated-sources/java</outputDirectory>
                            <processor>com.github.microtweak.jac4e.processor.Jac4eProcessor</processor>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

2. Annotate your enum and create the "value" property. The type and content of this property will be persisted in the database.

```java
@EnumAttributeConverter
public enum Status {

    PENDING('P'),

    SHIPPED('S'),

    COMPLETED('C');

    private char value; // Pay attention: the APT uses it for conversion
    
    Status(char value) {
        this.value = value;
    }
}
```

3. Declare the enum in the entity as a common property

```java
@Entity
public class Order {

    @Id
    private Long id;

    private Payment paymentMode; // Now you should NOT use @Enumerated

    private double total;

    // Getters and Setters

}
```