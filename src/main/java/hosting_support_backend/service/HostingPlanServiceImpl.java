package hosting_support_backend.service;

import hosting_support_backend.dto.requests.HostingPlanRequestDTO;
import hosting_support_backend.entity.HostingPlan;
import hosting_support_backend.repository.HostingPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class HostingPlanServiceImpl implements HostingPlanService{

  private final HostingPlanRepository hostingPlanRepository;


  @Override
  public HostingPlan create(HostingPlanRequestDTO dto){
    HostingPlan hostingPlan = HostingPlan.builder()
                              .name(dto.getName())
                              .description(dto.getDescription())
                              .price(dto.getPrice())
                              .storage(dto.getStorage())
                              .bandwidth(dto.getBandwidth())
                              .build();
    return hostingPlanRepository.save(hostingPlan);
  }

  @Override

  public  HostingPlan update(Long id, HostingPlanRequestDTO dto){
     HostingPlan existing = hostingPlanRepository.findById(id)
                .orElseThrow(() -> 
                new RuntimeException("HostingPlan not found with id: " + id));
    existing.setName(dto.getName());
    existing.setDescription(dto.getDescription());
    existing.setPrice(dto.getPrice());
    existing.setStorage(dto.getStorage());
    existing.setBandwidth(dto.getBandwidth());


    return hostingPlanRepository.save(existing);
  }



  @Override
  public void delete(Long id){
    if(!hostingPlanRepository.existsById(id)){
      throw new RuntimeException("HostingPlan not found with id :"+id);
    }
    hostingPlanRepository.deleteById(id);}


    @Override
    public HostingPlan getById(Long id){
      return hostingPlanRepository.findById(id)
              .orElseThrow(()-> new RuntimeException("HostingPlan not found with id: " + id));
    }

    @Override
    public List<HostingPlan> getAll(){
      return hostingPlanRepository.findAll();
    }

  
}
