package org.covid.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.covid.inventory.audit.Auditable;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "item_category")
@EntityListeners(AuditingEntityListener.class)
public class ItemCategory extends Auditable<String> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "item_category_name")
	private String itemCategoryName;
	
	@Column(nullable = false, name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the itemCategoryName
	 */
	public String getItemCategoryName() {
		return itemCategoryName;
	}

	/**
	 * @param itemCategoryName the itemCategoryName to set
	 */
	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
}
