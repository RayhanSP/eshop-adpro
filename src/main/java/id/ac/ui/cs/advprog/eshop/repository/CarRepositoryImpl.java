package id.ac.ui.cs.advprog.eshop.repository;
import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class CarRepositoryImpl implements CarRepository {
    private final List<Car> carData = new ArrayList<>();

    @Override
    public Car create(Car car) {
        if (car.getCarId() == null) {
            car.setCarId(UUID.randomUUID().toString());
        }
        carData.add(car);
        return car;
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(carData);
    }

    @Override
    public Car findById(String id) {
        return carData.stream()
                .filter(car -> car.getCarId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Car update(String id, Car updatedCar) {
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());
                return car;
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}
