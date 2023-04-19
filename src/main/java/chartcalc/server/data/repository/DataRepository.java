package chartcalc.server.data.repository;

import org.springframework.data.repository.CrudRepository;

import chartcalc.server.data.model.Data;

public interface DataRepository extends CrudRepository<Data, String> {

}
