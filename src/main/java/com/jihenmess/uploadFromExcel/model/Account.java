package com.jihenmess.uploadFromExcel.model;

import com.jihenmess.uploadFromExcel.enumeration.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Jihen.Messaabi
 */
@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Integer age;

}
