package com.itheima.bolee.insurance.validator.anno;

/**
 * IdentityCard
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
import com.itheima.bolee.insurance.validator.IdentityCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityCardValidator.class)
@Documented
public @interface IdentityCard {
    String message() default "错误的身份证号类型";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

