package org.covid.inventory.audit;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

import org.covid.inventory.entity.Audit;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 
 * @author mayank_gupta
 *
 * @param <U>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @CreatedBy
    protected U createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date createdDate;

    @LastModifiedBy
    protected U modifiedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date modifiedDate;

    public U getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public U getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(U modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getmodifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    @PrePersist
    public void onPrePersist() {
        audit("insert");
    }
      
    @PreUpdate
    public void onPreUpdate() {
        audit("update");
    }
      
    @PreRemove
    public void onPreRemove() {
        audit("delete");
    }
      
    private void audit(String operation) {
    	EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new Audit(operation, operation + " " + this.getClass().getSimpleName()));
    }
}