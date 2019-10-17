解压缩依赖  
```xml
        <dependency>
            <groupId>org.apache.ant</groupId>
            <artifactId>ant</artifactId>
            <version>1.8.1</version>
        </dependency>
```
```xml
        <dependency>
            <groupId>com.github.junrar</groupId>
            <artifactId>junrar</artifactId>
            <version>0.7</version>
        </dependency>
```

Base64工具依赖
```xml
<dependency>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.11</version>
</dependency>
```
>将byte[]转换为Base64String
* Base64.encodeBase64String(byte[] binaryData)
