package com.devopsusa.orderservice.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "TBL_ORDERS")
@ApiModel(description = "Details about the order")
public class Order {

    @Id
    @ApiModelProperty(notes = "The unique id of the order")
    private int id;
    @ApiModelProperty(notes = "The name of item to buy")
    private String name;
    @ApiModelProperty(notes = "The quantity of item")
    private int qty;
    @ApiModelProperty(notes = "The price of item")
    private double price;

}
