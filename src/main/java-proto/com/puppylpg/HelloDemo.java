package com.puppylpg;

/**
 * @author puppylpg on 2020/05/14
 */
public class HelloDemo {

    public static void main(String... args) {
        HelloWorld.Person father = HelloWorld.Person.newBuilder()
                .setId(1)
                .setName("father")
                .build();

        HelloWorld.Person mother = HelloWorld.Person.newBuilder()
                .setId(2)
                .setName("mother")
                .build();

        HelloWorld.Person son = HelloWorld.Person.newBuilder()
                .setId(3)
                .setName("son")
                .build();

        HelloWorld.Family family = HelloWorld.Family.newBuilder()
                .setAddress("zk")
                .addPeople(father)
                .addPeople(mother)
                .addPeople(son)
                .setCost(100)
                .putRoomSizes("room1", 10)
                .putRoomSizes("room2", 20)
                .build();

        System.out.println(family);
    }
}
