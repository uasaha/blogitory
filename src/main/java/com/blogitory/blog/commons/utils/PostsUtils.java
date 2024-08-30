package com.blogitory.blog.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * Posts utils.
 *
 * @author woonseok
 * @Date 2024-08-30
 * @since 1.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostsUtils {

  private static final int MAX_SUMMARY_LENGTH = 300;

  /**
   * Extract summary from details.
   *
   * @param details posts details
   * @return summary
   */
  public static String extractSummary(String details) {
    Parser parser = Parser.builder().build();
    Node detailNode = parser.parse(details);
    TextContentRenderer renderer = TextContentRenderer.builder().build();

    String summary = renderer.render(detailNode);
    return summary.substring(0, summary.length() > MAX_SUMMARY_LENGTH
            ? MAX_SUMMARY_LENGTH : summary.length() - 1);
  }
}
