package com.wonder.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author DelLevin
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestTable implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer id;

    private String name;

    private String tel;


}
