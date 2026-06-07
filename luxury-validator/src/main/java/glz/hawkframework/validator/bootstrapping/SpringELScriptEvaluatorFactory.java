package glz.hawkframework.validator.bootstrapping;

import org.hibernate.validator.spi.scripting.AbstractCachingScriptEvaluatorFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluationException;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.hibernate.validator.spi.scripting.ScriptEvaluatorNotFoundException;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.Map.Entry;

public class SpringELScriptEvaluatorFactory extends AbstractCachingScriptEvaluatorFactory {

    @Override
    protected ScriptEvaluator createNewScriptEvaluator(String languageName) throws ScriptEvaluatorNotFoundException {
        if (!"spring".equalsIgnoreCase(languageName)) {
            throw new IllegalStateException("Only Spring EL is supported");
        }

        return new SpringELScriptEvaluator();
    }

    private static class SpringELScriptEvaluator implements ScriptEvaluator {

        private final ExpressionParser expressionParser = new SpelExpressionParser();

        @Override
        public Object evaluate(String script, Map<String, Object> bindings) throws ScriptEvaluationException {
            try {
                Expression expression = expressionParser.parseExpression(script);
                EvaluationContext context = new StandardEvaluationContext(bindings.values().iterator().next());
                for (Entry<String, Object> binding : bindings.entrySet()) {
                    context.setVariable(binding.getKey(), binding.getValue());
                }
                return expression.getValue(context);
            } catch (ParseException | EvaluationException e) {
                throw new ScriptEvaluationException("Unable to evaluate SpEL script", e);
            }
        }

    }

}
