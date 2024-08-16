package fantastic.faniverse.keyword.repository;

import fantastic.faniverse.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fantastic.faniverse.user.entity.User;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    //List<Keyword> findByNameContaining(String name);
    List<Keyword> findByUser(User user);
}
