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

二进制上传文件
```java
    /**
     * 二进制上传文件
     *
     * @author CaoJing
     * @date 2019/10/9 15:20
     */
    @PostMapping(value = "/uploadFile2")
    @Transactional(rollbackFor = Exception.class)
    public Result uploadFile2(HttpServletRequest request) throws IOException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Result result = Result.success("上传文件成功");
        result.setData(ossService.uploadFileAutoType(request.getInputStream(), uuid, uuid + ".mp3").get(uuid));
        return result;
    }
```