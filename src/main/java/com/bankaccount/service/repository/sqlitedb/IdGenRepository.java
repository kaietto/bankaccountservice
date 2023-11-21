package com.bankaccount.service.repository.sqlitedb;

import com.bankaccount.service.entity.sqlitedb.IdGen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdGenRepository extends JpaRepository<IdGen, String>, JpaSpecificationExecutor<IdGen> {

    @Query(value = "SELECT MAX(nextid) + 1 FROM id_gen WHERE idname = :idname", nativeQuery = true)
    Long findNextIdPlusOne(String idname);
}
