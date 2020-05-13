package edu.whut.cs.jee.mooc.mclass.dto;

import com.sun.istack.internal.NotNull;
import edu.whut.cs.jee.mooc.common.persistence.Converter;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoocClassDto {

    private Long id;
    /**
     * 名称
     */
    @NotNull
    private String name;

    /**
     * 学年
     */
    @NotNull
    private String year;

    /**
     * 学期
     */
    @NotNull
    private String semester;

    /**
     * 关联课程ID
     */
    private Long courseId;

    /**
     * 独立线下课程名称
     */
    private String offlineCourse;

    public MoocClass convertTo(){
        MoocClassDTOConvert moocClassDTOConvert = new MoocClassDTOConvert();
        MoocClass convert = moocClassDTOConvert.doForward(this);
        return convert;
    }

    public MoocClassDto convertFor(MoocClass moocClass){
        MoocClassDTOConvert moocClassDTOConvert = new MoocClassDTOConvert();
        MoocClassDto convert = moocClassDTOConvert.doBackward(moocClass);
        return convert;
    }

    private static class MoocClassDTOConvert implements Converter<MoocClassDto, MoocClass> {

        public MoocClass doForward(MoocClassDto moocClassDto) {
            MoocClass moocClass = new MoocClass();
            BeanUtils.copyProperties(moocClassDto,moocClass);
            return moocClass;
        }

        public MoocClassDto doBackward(MoocClass moocClass) {
            MoocClassDto moocClassDto = new MoocClassDto();
            BeanUtils.copyProperties(moocClass,moocClassDto);
            return moocClassDto;
        }
    }
}
