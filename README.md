# MockHttpServer JSON matcher #

**This repo is archived and not maintained anymore. It is still available in read-only mode and can be forked.**

    <dependency>
        <groupId>com.github.kristofa</groupId>
        <artifactId>mock-http-server-json-matcher</artifactId>
        <version>1.0</version>
        <scope>test</scope>
    </dependency>


As of version 4.0, [mock-http-server](https://github.com/kristofa/mock-http-server)
is extended to support matching variable, non deterministic content in http requests.

An example of variable content can be a JSON entity.
JSON does not specify a fixed order of properties so when serializing JSON the order of
the properties can be different from 1 run to the other.

By default mock-http-server does an exact match of http requests, including entities. 
By using the `JsonMatchingFilter` the entity will be interpreted as json and the comparison
will also be done for json which means different order of properties will not be considered
as a difference anymore.

The `JsonMatchingFilter` implementation depends on [jackson-databind](https://github.com/FasterXML/jackson-databind)
dependency. I did not want to pull this dependency in mock-http-server so I created a separate module.

Similar implementations could be developed for XML or other entity formats.

## Changelog ##

### 1.0 ###

+   Initial version
