function show(elements_in, layout_in) {
    // set cytoscape to div
    var cy = cytoscape({
        container: document.getElementById('cy'),
        style: cytoscape.stylesheet()
            .selector('node')
            .css({
                'label': 'data(id)',
                'text-valign': 'center',
                'text-halign': 'center',
                'border-color': 'black',
                'border-opacity': '1',
            })
            .selector('edge')
            .css({
                'curve-style': 'bezier',
                'haystack-radius': .5,
            }),
        elements: elements_in,
        layout: layout_in
    });

    cy.edges().forEach(function( ele ){
        if (ele.data('label') == 'edge1') {
            ele.style('line-color', 'red');
        }
        else if (ele.data('label') == 'edge2') {
            ele.style('line-color', 'blue');
        }
        else if (ele.data('label') == 'edge3') {
            ele.style('line-color', 'green');
        }
        else if (ele.data('label') == 'b') {
                    ele.style('line-color', 'black');
        }
    });
}