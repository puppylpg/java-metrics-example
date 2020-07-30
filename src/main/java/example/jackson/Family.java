package example.jackson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

/**
 * @author liuhaibo on 2017/11/29
 */
public class Family {

    public static void main(String[] args) throws Exception {
        List<Father> dataList = new ArrayList<>();
        dataList.add(new Son2("10", 5));
        dataList.add(new Son1(8, 5));
        D d = new D();
        d.setList(dataList);

        ObjectMapper mapper = new ObjectMapper();

        String data = mapper.writeValueAsString(d);
        System.out.println(data);
        D result = mapper.readValue(data, D.class);

        System.out.println(result.getList());
    }

    public static class D {
        List<Father> list;

        public List<Father> getList() {
            return list;
        }

        public void setList(List<Father> list) {
            this.list = list;
        }

    }

    @ToString
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "custom-type-name")
    @JsonSubTypes(value = {
            @JsonSubTypes.Type(value = Son1.class, name = "FirstSon"),
            @JsonSubTypes.Type(value = Son2.class, name = "SecondSon")
    })
    public static class Father {
        protected int a;

        public Father() {
        }

        public Father(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

    }

    @ToString(callSuper = true)
    public static class Son1 extends Father {
        public Son1() {
        }

        public Son1(int b, int a) {
            super(a);
        }

        private int b;

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    @ToString(callSuper = true)
    public static class Son2 extends Father {
        private String c;

        public Son2() {
        }

        public Son2(String c, int a) {
            super(a);
            this.c = c;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

    }
}

