package org.broadinstitute.sting.gatk.walkers.annotator;

import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.contexts.StratifiedAlignmentContext;
import org.broadinstitute.sting.gatk.contexts.variantcontext.VariantContext;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.annotator.interfaces.InfoFieldAnnotation;
import org.broadinstitute.sting.gatk.walkers.annotator.interfaces.StandardAnnotation;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileup;
import org.broadinstitute.sting.utils.genotype.vcf.VCFInfoHeaderLine;

import java.util.Map;


public class SpanningDeletions implements InfoFieldAnnotation, StandardAnnotation {

    public String annotate(RefMetaDataTracker tracker, ReferenceContext ref, Map<String, StratifiedAlignmentContext> stratifiedContexts, VariantContext vc) {
        int deletions = 0;
        int depth = 0;
        for ( String sample : stratifiedContexts.keySet() ) {
            ReadBackedPileup pileup = stratifiedContexts.get(sample).getContext(StratifiedAlignmentContext.StratifiedContextType.COMPLETE).getBasePileup();
            deletions += pileup.getNumberOfDeletions();
            depth += pileup.size();
        }
        return String.format("%.2f", depth == 0 ? 0.0 : (double)deletions/(double)depth);
    }

    public String getKeyName() { return "Dels"; }

    public VCFInfoHeaderLine getDescription() { return new VCFInfoHeaderLine("Dels", 1, VCFInfoHeaderLine.INFO_TYPE.Float, "Fraction of Reads Containing Spanning Deletions"); }
}