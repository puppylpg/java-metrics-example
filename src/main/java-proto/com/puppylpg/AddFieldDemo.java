package com.puppylpg;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 3是新协议新增字段，使用旧协议解析的话，key-value pair可以解析出field number和value，
 * 但是字段的实际类型和名称只能从.proto源文件取得。所以这里名称不知道是啥。
 * 至于为类型倒是可以根据key里存储的protobuf存储类型确定个八九不离十。
 *
 * id: 1
 * name: "new"
 * 3: "title"
 *
 * @author liuhaibo on 2020/05/14
 */
public class AddFieldDemo {

    public static void main(String... args) throws InvalidProtocolBufferException {
        HelloWorld2.Person newPerson = HelloWorld2.Person.newBuilder()
                .setId(1)
                .setName("new")
                .setTitle("title")
                .build();

        HelloWorld.Person oldPerson = HelloWorld.Person.parseFrom(newPerson.toByteArray());
        System.out.println(oldPerson);
    }
}
