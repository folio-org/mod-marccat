package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Container for BibliographicRecord and Search.
 * <p>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"bibliographicRecord", "recordTemplate"})
public class ContainerRecordTemplate {

  @JsonProperty("bibliographicRecord")
  private BibliographicRecord bibliographicRecord;

  @JsonProperty("recordTemplate")
  private RecordTemplate recordTemplate;

  /**
   * @return the bibliographic record.
   */
  public BibliographicRecord getBibliographicRecord() {
    return bibliographicRecord;
  }

  /**
   * @param bibliographicRecord the bibliographic record.
   */
  public void setBibliographicRecord(BibliographicRecord bibliographicRecord) {
    this.bibliographicRecord = bibliographicRecord;
  }

  /**
   * @return the record template.
   */
  public RecordTemplate getRecordTemplate() {
    return recordTemplate;
  }

  /**
   * @param recordTemplate the record template.
   */
  public void setRecordTemplate(RecordTemplate recordTemplate) {
    this.recordTemplate = recordTemplate;
  }

}
