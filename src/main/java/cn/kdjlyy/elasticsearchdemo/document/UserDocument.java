package cn.kdjlyy.elasticsearchdemo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {
    private String id;
    private String name;
    private String sex;
    private Integer age;
    private String city;
}
