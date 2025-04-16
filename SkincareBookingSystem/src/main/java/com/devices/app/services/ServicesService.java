package com.devices.app.services;

import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Services;
import com.devices.app.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {
    private final ServiceRepository serviceRepository;
    private final FileService fileService;

    public ServicesService(ServiceRepository serviceRepository, FileService fileService) {
        this.serviceRepository = serviceRepository;
        this.fileService = fileService;
    }

    public ApiResponse<List<Services>> findAll() {
        List<Services> services = serviceRepository.findAll();
        return new ApiResponse<>(200, "Lấy danh sách dịch vụ thành công", services);
    }

    public ApiResponse<Services> deleteByID(Integer id) {
        Optional<Services> optionalService = serviceRepository.findById(id);
        if(optionalService.isEmpty()){
            return new ApiResponse<>(100, "Dịch vụ không tồn tại", null);
        }
        String illu = optionalService.get().getIllustration();
        if (illu != null) {
            fileService.deleteFile(illu);
        }
        serviceRepository.deleteById(id);
        return new ApiResponse<>(200, "Xóa dịch vụ thành công", null);
    }
    public ApiResponse<Services> createOrUpdate(Services serviceRequest, MultipartFile file) {
        if(serviceRequest.getId() != null) {
            Optional<Services> optionalService = serviceRepository.findById(serviceRequest.getId());
            Services service = optionalService.get();
            if (serviceRequest.getServiceName() != null) {
                service.setServiceName(serviceRequest.getServiceName());
            }
            if (serviceRequest.getDuration() != null) {
                service.setDuration(serviceRequest.getDuration());
            }
            if (serviceRequest.getDescription() != null) {
                service.setDescription(serviceRequest.getDescription());
            }
            if (serviceRequest.getPrice() != null) {
                service.setPrice(serviceRequest.getPrice());
            }
            if (serviceRequest.getCategoryID() == null) {
                service.setCategoryID(1);
            }
            if (file != null && !file.isEmpty()) {
                fileService.deleteFile(service.getIllustration());
                String fileUrl = fileService.uploadFile(file, "Uploads/Illustrations");
                if (!fileUrl.isEmpty()) {
                    service.setIllustration(fileUrl);
                }
            }
            serviceRepository.save(service);
            return new ApiResponse<>(200, "Cập nhật dịch vụ thành công", service);
        }
        Services service = new Services();
        service.setServiceName(serviceRequest.getServiceName());
        service.setDuration(serviceRequest.getDuration());
        service.setDescription(serviceRequest.getDescription());
        service.setPrice(serviceRequest.getPrice());
        service.setCategoryID(serviceRequest.getCategoryID());
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileService.uploadFile(file, "Uploads/Illustrations");
            if (!fileUrl.isEmpty()) {
                service.setIllustration(fileUrl);
            }
        }
        serviceRepository.save(service);
        return new ApiResponse<>(200, "Tạo mới dịch vụ thành công", service);
    }
}
