package ryzendee.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ryzendee.app.models.Deal;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Deal.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealRepository extends JpaRepository<Deal, UUID> {

    /**
     * Выполняет поиск сделок с фильтрацией и постраничной разбивкой.
     *
     * @param specification критерии фильтрации сделок
     * @param pageable     параметры постраничной разбивки и сортировки
     * @return страница сделок, удовлетворяющих критериям
     */
    Page<Deal> findAll(Specification<Deal> specification, Pageable pageable);

    /**
     * Находит активную сделку по уникальному идентификатору с загрузкой связанных сущностей.
     *
     * @param id уникальный идентификатор сделки
     * @return Optional с найденной сделкой или пустой, если не найдено
     */
    @EntityGraph(attributePaths = {"type", "status", "sums"})
    @Query("SELECT d FROM Deal d WHERE d.id = :id AND d.isActive = true")
    Optional<Deal> findByIdWithGraph(@Param("id") UUID id);

}
