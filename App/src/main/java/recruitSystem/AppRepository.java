package recruitSystem;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AppRepository extends PagingAndSortingRepository<App, Long>{

    List<App> findByUserId(Long userId);
}