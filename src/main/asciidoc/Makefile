DOCS       := cldoc doc

.PHONY: $(DOCS)

doc: ./report.adoc
	asciidoctor-pdf -r asciidoctor-diagram ./report.adoc \
 -a pdf-themesdir=. -a pdf-theme=report \
 -a pdf-fontsdir="fonts/FiraCode;GEM_FONTS_DIR" -o docs/report.pdf

cldoc:
	rm -f ./*.pdf ./.asciidoc/*
