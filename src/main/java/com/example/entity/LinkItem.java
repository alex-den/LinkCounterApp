package com.example.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LinkItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String url;
}
