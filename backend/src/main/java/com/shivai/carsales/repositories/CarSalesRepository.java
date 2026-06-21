package com.shivai.carsales.repositories;

import com.shivai.carsales.dto.MonthlyCountDto;
import com.shivai.carsales.dto.YearlyCountDto;
import com.shivai.carsales.entities.CarSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSalesRepository extends JpaRepository<CarSales,Long> {

    Boolean existsByCarNumber(String carNumber);

    @Query("""
        select new com.shivai.carsales.dto.YearlyCountDto(c.year,count(c))
        from CarSales c
        group by c.year
        order by c.year
""")
    List<YearlyCountDto> getYearlyCount();

    @Query("""
    select new com.shivai.carsales.dto.MonthlyCountDto(
    month (c.dateOfPurchase),count(c)
    )
    from CarSales c
    where YEAR (c.dateOfPurchase)=:year
    group by month (c.dateOfPurchase)
    order by month(c.dateOfPurchase)
""")
    List<MonthlyCountDto> getMonthlyCountByYear(@Param("year") int year);


}
