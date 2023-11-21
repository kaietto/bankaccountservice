package com.bankaccount.service.entity.sqlitedb;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IdGen")
public class IdGen implements Serializable {
    @Serial private static final long serialVersionUID = 20231121_2001L;

    @Id
    private String idname;
    private Long nextid;
}
