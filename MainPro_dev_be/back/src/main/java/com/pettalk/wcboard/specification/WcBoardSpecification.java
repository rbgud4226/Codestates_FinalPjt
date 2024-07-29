package com.pettalk.wcboard.specification;

import com.pettalk.wcboard.dto.WcBoardDto;
import com.pettalk.wcboard.entity.WcBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class WcBoardSpecification {

    public static Specification<WcBoard> equalWcTagWithTag(String wcTag) {
        return new Specification<WcBoard>() {
            @Override
            public Predicate toPredicate(Root<WcBoard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("wcTag"), wcTag);
            }
        };
    }

    public static Specification<WcBoard> equalAnimalTagWithTag(String animalTag) {
        return new Specification<WcBoard>() {
            @Override
            public Predicate toPredicate(Root<WcBoard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("animalTag"), animalTag);
            }
        };
    }

    public static Specification<WcBoard> equalAreaTagWithTag(String areaTag) {
        return new Specification<WcBoard>() {
            @Override
            public Predicate toPredicate(Root<WcBoard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("areaTag"), areaTag);
            }
        };
    }

}
